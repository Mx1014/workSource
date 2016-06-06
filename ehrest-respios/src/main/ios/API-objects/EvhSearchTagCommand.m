//
// EvhSearchTagCommand.m
//
#import "EvhSearchTagCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchTagCommand
//

@implementation EvhSearchTagCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSearchTagCommand* obj = [EvhSearchTagCommand new];
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
    if(self.keyword)
        [jsonObject setObject: self.keyword forKey: @"keyword"];
    if(self.serviceType)
        [jsonObject setObject: self.serviceType forKey: @"serviceType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.keyword = [jsonObject objectForKey: @"keyword"];
        if(self.keyword && [self.keyword isEqual:[NSNull null]])
            self.keyword = nil;

        self.serviceType = [jsonObject objectForKey: @"serviceType"];
        if(self.serviceType && [self.serviceType isEqual:[NSNull null]])
            self.serviceType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
