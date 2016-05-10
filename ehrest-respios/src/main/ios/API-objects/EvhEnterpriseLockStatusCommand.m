//
// EvhEnterpriseLockStatusCommand.m
//
#import "EvhEnterpriseLockStatusCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseLockStatusCommand
//

@implementation EvhEnterpriseLockStatusCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseLockStatusCommand* obj = [EvhEnterpriseLockStatusCommand new];
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
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.lockStatus)
        [jsonObject setObject: self.lockStatus forKey: @"lockStatus"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.lockStatus = [jsonObject objectForKey: @"lockStatus"];
        if(self.lockStatus && [self.lockStatus isEqual:[NSNull null]])
            self.lockStatus = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
