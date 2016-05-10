//
// EvhVersionUrlResponse.m
//
#import "EvhVersionUrlResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionUrlResponse
//

@implementation EvhVersionUrlResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVersionUrlResponse* obj = [EvhVersionUrlResponse new];
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
    if(self.downloadUrl)
        [jsonObject setObject: self.downloadUrl forKey: @"downloadUrl"];
    if(self.infoUrl)
        [jsonObject setObject: self.infoUrl forKey: @"infoUrl"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.downloadUrl = [jsonObject objectForKey: @"downloadUrl"];
        if(self.downloadUrl && [self.downloadUrl isEqual:[NSNull null]])
            self.downloadUrl = nil;

        self.infoUrl = [jsonObject objectForKey: @"infoUrl"];
        if(self.infoUrl && [self.infoUrl isEqual:[NSNull null]])
            self.infoUrl = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
