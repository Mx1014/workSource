//
// EvhClaimAddressCommand.m
//
#import "EvhClaimAddressCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhClaimAddressCommand
//

@implementation EvhClaimAddressCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhClaimAddressCommand* obj = [EvhClaimAddressCommand new];
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
    if(self.replacedAddressId)
        [jsonObject setObject: self.replacedAddressId forKey: @"replacedAddressId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.buildingName)
        [jsonObject setObject: self.buildingName forKey: @"buildingName"];
    if(self.apartmentName)
        [jsonObject setObject: self.apartmentName forKey: @"apartmentName"];
    if(self.historyId)
        [jsonObject setObject: self.historyId forKey: @"historyId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.replacedAddressId = [jsonObject objectForKey: @"replacedAddressId"];
        if(self.replacedAddressId && [self.replacedAddressId isEqual:[NSNull null]])
            self.replacedAddressId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.buildingName = [jsonObject objectForKey: @"buildingName"];
        if(self.buildingName && [self.buildingName isEqual:[NSNull null]])
            self.buildingName = nil;

        self.apartmentName = [jsonObject objectForKey: @"apartmentName"];
        if(self.apartmentName && [self.apartmentName isEqual:[NSNull null]])
            self.apartmentName = nil;

        self.historyId = [jsonObject objectForKey: @"historyId"];
        if(self.historyId && [self.historyId isEqual:[NSNull null]])
            self.historyId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
