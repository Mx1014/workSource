//
// EvhListOrganizationsByNameCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrganizationsByNameCommand
//
@interface EvhListOrganizationsByNameCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

