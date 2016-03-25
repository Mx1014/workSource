//
// EvhListOrganizationsCommand.h
// generated at 2016-03-25 09:26:38 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOrganizationsCommand
//
@interface EvhListOrganizationsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* organizationType;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

