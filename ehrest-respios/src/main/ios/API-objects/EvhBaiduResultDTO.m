//
// EvhBaiduResultDTO.m
//
#import "EvhBaiduResultDTO.h"
#import "EvhBaiduAddressComponentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBaiduResultDTO
//

@implementation EvhBaiduResultDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBaiduResultDTO* obj = [EvhBaiduResultDTO new];
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
    if(self.addressComponent) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.addressComponent toJson: dic];
        
        [jsonObject setObject: dic forKey: @"addressComponent"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"addressComponent"];

        self.addressComponent = [EvhBaiduAddressComponentDTO new];
        self.addressComponent = [self.addressComponent fromJson: itemJson];
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
