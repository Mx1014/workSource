//
// EvhUpdateBuildingAdminCommand.m
//
#import "EvhUpdateBuildingAdminCommand.h"
#import "EvhForumAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateBuildingAdminCommand
//

@implementation EvhUpdateBuildingAdminCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUpdateBuildingAdminCommand* obj = [EvhUpdateBuildingAdminCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.aliasName)
        [jsonObject setObject: self.aliasName forKey: @"aliasName"];
    if(self.managerUid)
        [jsonObject setObject: self.managerUid forKey: @"managerUid"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.areaSize)
        [jsonObject setObject: self.areaSize forKey: @"areaSize"];
    if(self.geoString)
        [jsonObject setObject: self.geoString forKey: @"geoString"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.posterUri)
        [jsonObject setObject: self.posterUri forKey: @"posterUri"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.attachments) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhForumAttachmentDescriptor* item in self.attachments) {
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
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.aliasName = [jsonObject objectForKey: @"aliasName"];
        if(self.aliasName && [self.aliasName isEqual:[NSNull null]])
            self.aliasName = nil;

        self.managerUid = [jsonObject objectForKey: @"managerUid"];
        if(self.managerUid && [self.managerUid isEqual:[NSNull null]])
            self.managerUid = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.areaSize = [jsonObject objectForKey: @"areaSize"];
        if(self.areaSize && [self.areaSize isEqual:[NSNull null]])
            self.areaSize = nil;

        self.geoString = [jsonObject objectForKey: @"geoString"];
        if(self.geoString && [self.geoString isEqual:[NSNull null]])
            self.geoString = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.posterUri = [jsonObject objectForKey: @"posterUri"];
        if(self.posterUri && [self.posterUri isEqual:[NSNull null]])
            self.posterUri = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"attachments"];
            for(id itemJson in jsonArray) {
                EvhForumAttachmentDescriptor* item = [EvhForumAttachmentDescriptor new];
                
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
