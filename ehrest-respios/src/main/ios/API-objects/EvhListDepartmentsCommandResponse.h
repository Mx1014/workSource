//
// EvhListDepartmentsCommandResponse.h
// generated at 2016-04-19 13:40:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhDepartmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListDepartmentsCommandResponse
//
@interface EvhListDepartmentsCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhDepartmentDTO*
@property(nonatomic, strong) NSMutableArray* departments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

