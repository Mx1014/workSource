//
// EvhBaiduGeocoderResponse.m
//
#import "EvhBaiduGeocoderResponse.h"
#import "EvhBaiduResultDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBaiduGeocoderResponse
//

@implementation EvhBaiduGeocoderResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBaiduGeocoderResponse* obj = [EvhBaiduGeocoderResponse new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.result) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.result toJson: dic];
        
        [jsonObject setObject: dic forKey: @"result"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"result"];

        self.result = [EvhBaiduResultDTO new];
        self.result = [self.result fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
