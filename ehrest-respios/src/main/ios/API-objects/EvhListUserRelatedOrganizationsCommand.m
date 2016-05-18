//
// EvhListUserRelatedOrganizationsCommand.m
//
#import "EvhListUserRelatedOrganizationsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserRelatedOrganizationsCommand
//

@implementation EvhListUserRelatedOrganizationsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListUserRelatedOrganizationsCommand* obj = [EvhListUserRelatedOrganizationsCommand new];
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
    if(self.organiztionType)
        [jsonObject setObject: self.organiztionType forKey: @"organiztionType"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.organiztionType = [jsonObject objectForKey: @"organiztionType"];
        if(self.organiztionType && [self.organiztionType isEqual:[NSNull null]])
            self.organiztionType = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
