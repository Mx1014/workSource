//
// EvhUpdateContactorCommand.m
//
#import "EvhUpdateContactorCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateContactorCommand
//

@implementation EvhUpdateContactorCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateContactorCommand* obj = [EvhUpdateContactorCommand new];
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
    if(self.contactorName)
        [jsonObject setObject: self.contactorName forKey: @"contactorName"];
    if(self.contactor)
        [jsonObject setObject: self.contactor forKey: @"contactor"];
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.contactorName = [jsonObject objectForKey: @"contactorName"];
        if(self.contactorName && [self.contactorName isEqual:[NSNull null]])
            self.contactorName = nil;

        self.contactor = [jsonObject objectForKey: @"contactor"];
        if(self.contactor && [self.contactor isEqual:[NSNull null]])
            self.contactor = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
