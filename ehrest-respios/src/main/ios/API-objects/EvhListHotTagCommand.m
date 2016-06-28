//
// EvhListHotTagCommand.m
//
#import "EvhListHotTagCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListHotTagCommand
//

@implementation EvhListHotTagCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListHotTagCommand* obj = [EvhListHotTagCommand new];
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
    if(self.pageSize)
        [jsonObject setObject: self.pageSize forKey: @"pageSize"];
    if(self.serviceType)
        [jsonObject setObject: self.serviceType forKey: @"serviceType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.pageSize = [jsonObject objectForKey: @"pageSize"];
        if(self.pageSize && [self.pageSize isEqual:[NSNull null]])
            self.pageSize = nil;

        self.serviceType = [jsonObject objectForKey: @"serviceType"];
        if(self.serviceType && [self.serviceType isEqual:[NSNull null]])
            self.serviceType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
