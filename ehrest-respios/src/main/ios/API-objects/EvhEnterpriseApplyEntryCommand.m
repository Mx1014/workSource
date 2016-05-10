//
// EvhEnterpriseApplyEntryCommand.m
//
#import "EvhEnterpriseApplyEntryCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseApplyEntryCommand
//

@implementation EvhEnterpriseApplyEntryCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhEnterpriseApplyEntryCommand* obj = [EvhEnterpriseApplyEntryCommand new];
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
    if(self.sourceId)
        [jsonObject setObject: self.sourceId forKey: @"sourceId"];
    if(self.sourceType)
        [jsonObject setObject: self.sourceType forKey: @"sourceType"];
    if(self.enterpriseName)
        [jsonObject setObject: self.enterpriseName forKey: @"enterpriseName"];
    if(self.enterpriseId)
        [jsonObject setObject: self.enterpriseId forKey: @"enterpriseId"];
    if(self.applyUserName)
        [jsonObject setObject: self.applyUserName forKey: @"applyUserName"];
    if(self.contactPhone)
        [jsonObject setObject: self.contactPhone forKey: @"contactPhone"];
    if(self.applyType)
        [jsonObject setObject: self.applyType forKey: @"applyType"];
    if(self.sizeUnit)
        [jsonObject setObject: self.sizeUnit forKey: @"sizeUnit"];
    if(self.areaSize)
        [jsonObject setObject: self.areaSize forKey: @"areaSize"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sourceId = [jsonObject objectForKey: @"sourceId"];
        if(self.sourceId && [self.sourceId isEqual:[NSNull null]])
            self.sourceId = nil;

        self.sourceType = [jsonObject objectForKey: @"sourceType"];
        if(self.sourceType && [self.sourceType isEqual:[NSNull null]])
            self.sourceType = nil;

        self.enterpriseName = [jsonObject objectForKey: @"enterpriseName"];
        if(self.enterpriseName && [self.enterpriseName isEqual:[NSNull null]])
            self.enterpriseName = nil;

        self.enterpriseId = [jsonObject objectForKey: @"enterpriseId"];
        if(self.enterpriseId && [self.enterpriseId isEqual:[NSNull null]])
            self.enterpriseId = nil;

        self.applyUserName = [jsonObject objectForKey: @"applyUserName"];
        if(self.applyUserName && [self.applyUserName isEqual:[NSNull null]])
            self.applyUserName = nil;

        self.contactPhone = [jsonObject objectForKey: @"contactPhone"];
        if(self.contactPhone && [self.contactPhone isEqual:[NSNull null]])
            self.contactPhone = nil;

        self.applyType = [jsonObject objectForKey: @"applyType"];
        if(self.applyType && [self.applyType isEqual:[NSNull null]])
            self.applyType = nil;

        self.sizeUnit = [jsonObject objectForKey: @"sizeUnit"];
        if(self.sizeUnit && [self.sizeUnit isEqual:[NSNull null]])
            self.sizeUnit = nil;

        self.areaSize = [jsonObject objectForKey: @"areaSize"];
        if(self.areaSize && [self.areaSize isEqual:[NSNull null]])
            self.areaSize = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
