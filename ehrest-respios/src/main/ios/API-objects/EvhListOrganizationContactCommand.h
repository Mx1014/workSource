//
// EvhListOrganizationContactCommand.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrganizationContactCommand
//
@interface EvhListOrganizationContactCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSString* keywords;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

