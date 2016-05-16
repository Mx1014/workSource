//
// EvhCreateLeasePromotionCommand.m
//
#import "EvhCreateLeasePromotionCommand.h"
#import "EvhBuildingForRentAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLeasePromotionCommand
//

@implementation EvhCreateLeasePromotionCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateLeasePromotionCommand* obj = [EvhCreateLeasePromotionCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _attachments = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.buildingId)
        [jsonObject setObject: self.buildingId forKey: @"buildingId"];
    if(self.rentPosition)
        [jsonObject setObject: self.rentPosition forKey: @"rentPosition"];
    if(self.rentType)
        [jsonObject setObject: self.rentType forKey: @"rentType"];
    if(self.posterUri)
        [jsonObject setObject: self.posterUri forKey: @"posterUri"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.rentAreas)
        [jsonObject setObject: self.rentAreas forKey: @"rentAreas"];
    if(self.contacts)
        [jsonObject setObject: self.contacts forKey: @"contacts"];
    if(self.contactPhone)
        [jsonObject setObject: self.contactPhone forKey: @"contactPhone"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.enterTime)
        [jsonObject setObject: self.enterTime forKey: @"enterTime"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhBuildingForRentAttachmentDTO* item in self.attachments) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"attachments"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.buildingId = [jsonObject objectForKey: @"buildingId"];
        if(self.buildingId && [self.buildingId isEqual:[NSNull null]])
            self.buildingId = nil;

        self.rentPosition = [jsonObject objectForKey: @"rentPosition"];
        if(self.rentPosition && [self.rentPosition isEqual:[NSNull null]])
            self.rentPosition = nil;

        self.rentType = [jsonObject objectForKey: @"rentType"];
        if(self.rentType && [self.rentType isEqual:[NSNull null]])
            self.rentType = nil;

        self.posterUri = [jsonObject objectForKey: @"posterUri"];
        if(self.posterUri && [self.posterUri isEqual:[NSNull null]])
            self.posterUri = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.rentAreas = [jsonObject objectForKey: @"rentAreas"];
        if(self.rentAreas && [self.rentAreas isEqual:[NSNull null]])
            self.rentAreas = nil;

        self.contacts = [jsonObject objectForKey: @"contacts"];
        if(self.contacts && [self.contacts isEqual:[NSNull null]])
            self.contacts = nil;

        self.contactPhone = [jsonObject objectForKey: @"contactPhone"];
        if(self.contactPhone && [self.contactPhone isEqual:[NSNull null]])
            self.contactPhone = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.enterTime = [jsonObject objectForKey: @"enterTime"];
        if(self.enterTime && [self.enterTime isEqual:[NSNull null]])
            self.enterTime = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhBuildingForRentAttachmentDTO* item = [EvhBuildingForRentAttachmentDTO new];
                
                [item fromJson: itemJson];
                [self.attachments addObject: item];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
