//
// EvhCreateOrganizationTaskCommand.h
// generated at 2016-03-31 13:49:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateOrganizationTaskCommand
//
@interface EvhCreateOrganizationTaskCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* taskStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

