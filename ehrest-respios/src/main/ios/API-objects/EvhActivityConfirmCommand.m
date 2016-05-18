//
// EvhActivityConfirmCommand.m
//
#import "EvhActivityConfirmCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityConfirmCommand
//

@implementation EvhActivityConfirmCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhActivityConfirmCommand* obj = [EvhActivityConfirmCommand new];
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
    if(self.rosterId)
        [jsonObject setObject: self.rosterId forKey: @"rosterId"];
    if(self.confirmFamilyId)
        [jsonObject setObject: self.confirmFamilyId forKey: @"confirmFamilyId"];
    if(self.targetName)
        [jsonObject setObject: self.targetName forKey: @"targetName"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.rosterId = [jsonObject objectForKey: @"rosterId"];
        if(self.rosterId && [self.rosterId isEqual:[NSNull null]])
            self.rosterId = nil;

        self.confirmFamilyId = [jsonObject objectForKey: @"confirmFamilyId"];
        if(self.confirmFamilyId && [self.confirmFamilyId isEqual:[NSNull null]])
            self.confirmFamilyId = nil;

        self.targetName = [jsonObject objectForKey: @"targetName"];
        if(self.targetName && [self.targetName isEqual:[NSNull null]])
            self.targetName = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
