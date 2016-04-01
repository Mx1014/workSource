//
// EvhListFamilyRequestsCommand.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFamilyRequestsCommand
//
@interface EvhListFamilyRequestsCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

